import {Component, OnInit} from '@angular/core';
import {NavigateDirective} from "../../navigate.directive";
import {KeyValuePipe, NgForOf, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {GlobalService} from "../../global.service";
import {BookService} from "../book.service";
import {EnumService} from "../../enum.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CategoryService} from "../../category/category.service";

@Component({
	selector: 'app-book-add',
	imports: [
		NavigateDirective,
		ReactiveFormsModule,
		NgForOf,
		KeyValuePipe,
		NgIf
	],
	templateUrl: './book-add.component.html',
	standalone: true
})

export class BookAddComponent implements OnInit {

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
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'MANAGER') this.router.navigate(['/login']);

		this.categoryService.categorySubject.subscribe(value => {
			this.categories = value.categories;
		})
		this.categoryService.findAll();

		this.enumService.enumSubject.subscribe(value => {
			this.ages = value.bookAges;
			this.types = value.bookTypes;
		})
		this.enumService.getBookAges();
		this.enumService.getBookTypes();
	}

	changeCategoryId(event: any) {
		this.categoryId = event.target.value;
	}

	changeAge(event: any) {
		this.age = event.target.value;
	}

	changeType(event: any) {
		this.type = event.target.value;
	}

	changeImg(event: any) {
		this.img = event.target.files;
	}

	changeFile(event: any) {
		this.file = event.target.files;
	}

	checkSubmit(): boolean {
		if (this.bookFormGroup.invalid) return false;
		if (this.categoryId === null) return false;
		if (this.age === '') return false;
		if (this.type === '') return false;
		if (this.img === null) return false;

		if (this.type === 'ELECTRONIC') {
			if (this.file === null) return false;
		}

		return true;
	}

	save() {
		this.bookService.save(this.bookFormGroup.value, this.categoryId, this.age, this.type, this.img, this.file);
	}

}
