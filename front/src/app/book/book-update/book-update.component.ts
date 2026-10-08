import {Component, OnInit} from '@angular/core';
import {KeyValuePipe, NgForOf, NgIf} from "@angular/common";
import {NavigateDirective} from "../../navigate.directive";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalService} from "../../global.service";
import {BookService} from "../book.service";
import {EnumService} from "../../enum.service";
import {CategoryService} from "../../category/category.service";

@Component({
	selector: 'app-book-update',
	imports: [
		KeyValuePipe,
		NavigateDirective,
		NgForOf,
		ReactiveFormsModule,
		NgIf
	],
	templateUrl: './book-update.component.html',
	standalone: true
})

export class BookUpdateComponent implements OnInit {

	id: number = 0;

	bookFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		author: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		page: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000000)]),
		date: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		read: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		price: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000000)]),
		quantity: new FormControl(0, [Validators.required, Validators.min(0), Validators.max(1000000)]),
		description: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(5000)]),
	})

	categories: any[] = [];
	categoryId: any = null;

	ages: any[] = [];
	age: string = '';

	types: any[] = [];
	type: string = '';

	img: any = null;
	file: any = null;

	constructor(
		private router: Router,
		private global: GlobalService,
		private bookService: BookService,
		private enumService: EnumService,
		private categoryService: CategoryService,
		private activatedRoute: ActivatedRoute,
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'MANAGER') this.router.navigate(['/login']);

		this.activatedRoute.queryParams.subscribe(params => {
			this.id = params['id'];
			this.bookService.find(params['id']).subscribe({
				next: (res: any) => {
					this.bookFormGroup.setValue({
						name: res.data.name,
						author: res.data.author,
						page: res.data.page,
						date: res.data.date,
						read: res.data.read,
						price: res.data.price,
						quantity: res.data.quantity,
						description: res.data.description,
					})
					this.categoryId = res.data.categoryId
					this.age = res.data.age
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

		this.categoryService.categorySubject.subscribe(value => {
			this.categories = value.categories;
		})
		this.categoryService.findAll();

		this.enumService.enumSubject.subscribe(value => {
			this.ages = value.bookAges;
		})
		this.enumService.getBookAges();
	}

	changeCategoryId(event: any) {
		this.categoryId = event.target.value;
	}

	changeAge(event: any) {
		this.age = event.target.value;
	}

	changeImg(event: any) {
		this.img = event.target.files;
	}

	checkSubmit(): boolean {
		if (this.bookFormGroup.invalid) return false;
		if (this.categoryId === null) return false;
		if (this.age === '') return false;

		return true;
	}

	update() {
		this.bookService.update(this.id, this.bookFormGroup.value, this.categoryId, this.age, this.type, this.img, this.file);
	}

}
