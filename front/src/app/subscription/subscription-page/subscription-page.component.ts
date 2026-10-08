import {Component, OnInit} from '@angular/core';
import {SubscriptionService} from "../subscription.service";
import {GlobalService} from "../../global.service";
import {SubscriptionOrderingService} from "../../subscription-ordering/subscription-ordering.service";
import {ActivatedRoute, Router} from "@angular/router";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NavigateDirective} from "../../navigate.directive";
import {NgIf} from "@angular/common";
import {AlertService} from "../../alert/alert.service";

@Component({
	selector: 'app-subscription-page',
	imports: [
		NavigateDirective,
		NgIf,
		FormsModule,
		ReactiveFormsModule
	],
	templateUrl: './subscription-page.component.html',
	standalone: true,
})

export class SubscriptionPageComponent implements OnInit {

	subscription: any = {
		name: '',
	};

	subscriptionActive: boolean = false;

	orderingFormGroup = new FormGroup({
		card: new FormControl("", [Validators.required, Validators.minLength(16), Validators.maxLength(16), Validators.pattern("[0-9]{16}")]),
		cvv: new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(3), Validators.pattern("[0-9]{3}")]),
	});

	orderings: any[] = [];

	constructor(
		private service: SubscriptionService,
		private global: GlobalService,
		private orderingService: SubscriptionOrderingService,
		private activatedRoute: ActivatedRoute,
		private router: Router,
		private alert: AlertService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		this.activatedRoute.queryParams.subscribe(params => {
			this.service.find(params['id']).subscribe({
				next: (res: any) => {
					this.subscription = res.data;
				},
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
			this.orderingService.active().subscribe({
				next: (res: any) => this.subscriptionActive = res.data,
				error: (e: any) => this.global.error(e)
			})
		}
	}

	delete() {
		this.service.delete(this.subscription.id)
	}

	orderingSave() {
		this.orderingService.save(this.orderingFormGroup.value, this.subscription.id).subscribe({
			next: () => {
				this.orderingFormGroup.reset();
				this.alert.showAlertMessage("Подписка оформлена!")
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
