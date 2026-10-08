import {Component, Input, OnInit} from '@angular/core';
import {CategoryService} from "../category.service";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
	selector: 'app-category-card',
	imports: [
		FormsModule,
		ReactiveFormsModule
	],
	templateUrl: './category-card.component.html',
	standalone: true
})

export class CategoryCardComponent implements OnInit {

	@Input() category: any;

	categoryFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
	})

	constructor(
		private categoryService: CategoryService,
	) {
	}

	ngOnInit(): void {
		this.categoryFormGroup.setValue({
			name: this.category.name,
		})
	}

	update() {
		this.categoryService.update(this.category.id, this.categoryFormGroup.value);
	}

	delete() {
		this.categoryService.delete(this.category.id);
	}
}
