import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../../global.service";
import {BookService} from "../book.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {DecimalPipe, NgIf} from "@angular/common";
import {NavigateDirective} from "../../navigate.directive";
import {BookCommentService} from "../book-comment.service";
import {DiscountService} from "../../discount/discount.service";
import {OrderingService} from "../../ordering/ordering.service";
import {AlertService} from "../../alert/alert.service";
import {BookPageCommentComponent} from "./book-page-comment/book-page-comment.component";
import {SubscriptionOrderingService} from "../../subscription-ordering/subscription-ordering.service";

@Component({
	selector: 'app-book-page',
	imports: [
		FormsModule,
		NavigateDirective,
		ReactiveFormsModule,
		NgIf,
		DecimalPipe,
		BookPageCommentComponent
	],
	templateUrl: './book-page.component.html',
	standalone: true
})

export class BookPageComponent implements OnInit {

	book: any = {
		name: '',
	};

	get comments() {
		return this.book.comments;
	}

	subscriptionActive:boolean = false;

	checkCommit() {
		for (let i of this.book.orderings) {
			if (i.ownerId === this.userid) {
				if (i.status === 'RETURNED') return true;
			}
		}
		return false;
	}

	commentFormGroup = new FormGroup({
		score: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(10)]),
		text: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(5000)]),
	})

	orderingFormGroup = new FormGroup({
		date: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		days: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(100)]),
	})

	discounts: any[] = [];
	discountId: number = 0;

	minDate: string = '';
	maxDate: string = '';

	constructor(
		private global: GlobalService,
		private bookService: BookService,
		private activatedRoute: ActivatedRoute,
		private router: Router,
		private commentService: BookCommentService,
		private discountService: DiscountService,
		private orderingService: OrderingService,
		private alert: AlertService,
		private subscriptionOrderingService:SubscriptionOrderingService,
	) {
	}

	get role() {
		return this.global.role;
	}

	get userid() {
		return this.global.userid;
	}

	ngOnInit(): void {
		const today = new Date();
		this.minDate = today.toISOString().split('T')[0];

		today.setDate(today.getDate() + 7);
		this.maxDate = today.toISOString().split('T')[0];

		this.activatedRoute.queryParams.subscribe(params => {
			this.bookService.find(params['id']).subscribe({
				next: (res: any) => this.book = res.data,
				error: (e: any) => {
					console.log(e.error)
					if (e.error.code === 404) {
						this.router.navigate(['/error'], {queryParams: {message: e.error.message}})
					} else {
						this.router.navigate(['/login'])
					}
				}
			})
		})

		if (this.role === 'USER') {
			this.discountService.discountSubject.subscribe(value => {
				this.discounts = value.discounts;
			})
			this.discountService.findAll();

			this.subscriptionOrderingService.active().subscribe({
				next: (res: any) => this.subscriptionActive = res.data,
				error: (e: any) => this.global.error(e)
			})
		}
	}

	updateRepair() {
		this.bookService.updateRepair(this.book.id).subscribe({
			next: (res: any) => this.book = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	visibility() {
		this.bookService.visibility(this.book.id).subscribe({
			next: (res: any) => this.book = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	delete() {
		this.bookService.delete(this.book.id);
	}

	comment() {
		this.commentService.save(this.commentFormGroup.value, this.book.id).subscribe({
			next: (res: any) => {
				this.book.comments.unshift(res.data);
				this.commentFormGroup.reset()
			},
			error: (e: any) => this.global.error(e),
		})
	}

	changeDiscountId(event: any) {
		this.discountId = event.target.value;
	}

	get orderingSum(): number {
		let percent: number = 0;

		if (this.discountId != 0) {
			for (let i of this.discounts) {
				if (i.id == this.discountId) {
					percent = i.percent;
					break;
				}
			}
		}

		let days: number = this.orderingFormGroup.value.days === null || this.orderingFormGroup.value.days === undefined ? 0 : this.orderingFormGroup.value.days;

		return (this.book.price * days) * ((100 - percent) / 100);
	};

	ordering() {
		this.orderingService.save(this.orderingFormGroup.value, this.book.id, this.discountId).subscribe({
			next: () => {
				this.orderingFormGroup.reset();
				this.alert.showAlertMessage("Заявка оформлена!");
				this.bookService.find(this.book.id).subscribe({
					next: (res: any) => this.book = res.data,
				})
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
