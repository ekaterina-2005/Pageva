import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {DiscountService} from "../discount.service";

@Component({
	selector: 'app-discount-card',
	imports: [
		ReactiveFormsModule
	],
	templateUrl: './discount-card.component.html',
	standalone: true
})

export class DiscountCardComponent implements OnInit {

	@Input() discount: any;

	discountFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		percent: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(100)]),
	})

	constructor(
		private discountService: DiscountService,
	) {
	}

	ngOnInit(): void {
		this.discountFormGroup.setValue({
			name: this.discount.name,
			percent: this.discount.percent,
		})
	}

	update() {
		this.discountService.update(this.discount.id, this.discountFormGroup.value);
	}

	delete() {
		this.discountService.delete(this.discount.id);
	}
}
