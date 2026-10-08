import {Component, OnInit} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {CategoryService} from "./category.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CategoryCardComponent} from "./category-card/category-card.component";

@Component({
	selector: 'app-category',
	imports: [
		ReactiveFormsModule,
		CategoryCardComponent
	],
	templateUrl: './category.component.html',
	standalone: true,
})

export class CategoryComponent implements OnInit {

	categories: any[] = [];

	categoryFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
	})

	constructor(
		private authService: AuthService,
		private router: Router,
		private global: GlobalService,
		private categoryService: CategoryService,
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'MANAGER') this.router.navigate(['/login']);

		this.categoryService.categorySubject.subscribe(value => {
			this.categories = value.categories;
		})
		this.categoryService.findAll();
	}

	save() {
		this.categoryService.save(this.categoryFormGroup.value);
		this.categoryFormGroup.reset();
	}
}
