import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../global.service";
import {OrderingService} from "./ordering.service";
import {Router} from "@angular/router";
import {NavigateDirective} from "../navigate.directive";
import {NgIf} from "@angular/common";
import {DiscountService} from "../discount/discount.service";
import {FormsModule} from "@angular/forms";

@Component({
	selector: 'app-ordering',
	imports: [
		NavigateDirective,
		NgIf,
		FormsModule
	],
	templateUrl: './ordering.component.html',
	standalone: true
})

export class OrderingComponent implements OnInit {

	orderings: any[] = [];

	discounts: any[] = [];

	constructor(
		private router: Router,
		private global: GlobalService,
		private orderingService: OrderingService,
		private discountService: DiscountService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		if (this.role !== 'MANAGER' && this.role !== 'USER') this.router.navigate(['/login']);

		this.orderingService.orderingsSubject.subscribe(value => {
			this.orderings = value.orderings;
		})
		this.orderingService.findAll();

		this.discountService.discountSubject.subscribe(value => {
			this.discounts = value.discounts;
		})
		this.discountService.findAll();
	}

	updateDiscount(id: number, event: any) {
		this.orderingService.updateDiscount(id, event.target.value);
	}

	booked(id: number) {
		this.orderingService.booked(id);
	}

	rejected(id: number) {
		this.orderingService.rejected(id);
	}

	done(id: number) {
		this.orderingService.done(id);
	}

	returned(id: number) {
		this.orderingService.returned(id);
	}

}
