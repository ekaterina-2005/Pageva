import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../global.service";
import {OrderingService} from "./ordering.service";
import {Router} from "@angular/router";
import {NavigateDirective} from "../navigate.directive";
import {NgIf} from "@angular/common";
import {DiscountService} from "../discount/discount.service";
import {FormsModule} from "@angular/forms";
import {BookCopyService} from "../book/book-copy.service";

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

	// свободные экземпляры по книгам и выбранный экземпляр по заявке (0 — автоматически)
	freeCopies: { [bookId: number]: any[] } = {};
	selectedCopy: { [orderingId: number]: number } = {};

	constructor(
		private router: Router,
		private global: GlobalService,
		private orderingService: OrderingService,
		private discountService: DiscountService,
		private bookCopyService: BookCopyService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		if (this.role !== 'MANAGER' && this.role !== 'USER') this.router.navigate(['/login']);

		this.orderingService.orderingsSubject.subscribe(value => {
			this.orderings = value.orderings;
			if (this.role === 'MANAGER') this.loadFreeCopies();
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

	loadFreeCopies() {
		const bookIds = new Set<number>(
			this.orderings.filter((i: any) => i.status === 'WAITING').map((i: any) => i.bookId)
		);
		bookIds.forEach(bookId => {
			this.bookCopyService.findAll(bookId).subscribe({
				next: (res: any) => this.freeCopies[bookId] = res.data.filter((c: any) => c.status === 'AVAILABLE'),
				error: (e: any) => this.global.error(e),
			})
		})
	}

	changeCopy(orderingId: number, event: any) {
		this.selectedCopy[orderingId] = Number(event.target.value);
	}

	booked(id: number) {
		this.orderingService.booked(id, this.selectedCopy[id] || 0);
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
