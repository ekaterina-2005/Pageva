import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../auth/auth.service";
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {DiscountService} from "./discount.service";
import {DiscountCardComponent} from "./discount-card/discount-card.component";

@Component({
	selector: 'app-discount',
	imports: [
		ReactiveFormsModule,
		DiscountCardComponent
	],
	templateUrl: './discount.component.html',
	standalone: true
})

export class DiscountComponent implements OnInit {

	discounts: any[] = [];

	discountFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		percent: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(100)]),
	})

	constructor(
		private authService: AuthService,
		private router: Router,
		private global: GlobalService,
		private discountService: DiscountService,
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'ADMIN') this.router.navigate(['/login']);

		this.discountService.discountSubject.subscribe(value => {
			this.discounts = value.discounts;
		})
		this.discountService.findAll();
	}

	save() {
		this.discountService.save(this.discountFormGroup.value);
		this.discountFormGroup.reset();
	}

}
