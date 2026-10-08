import {Component, OnInit} from '@angular/core';
import {NavigateDirective} from "../navigate.directive";
import {NgIf} from "@angular/common";
import {GlobalService} from "../global.service";
import {SubscriptionOrderingService} from "./subscription-ordering.service";
import {Router} from "@angular/router";
import {AlertService} from "../alert/alert.service";

@Component({
	selector: 'app-subscription-ordering',
	imports: [
		NavigateDirective,
		NgIf
	],
	templateUrl: './subscription-ordering.component.html',
	standalone: true,
})

export class SubscriptionOrderingComponent implements OnInit {

	orderings: any[] = [];

	get orderingsSorted() {
		let res = this.orderings;

		return res;
	}

	constructor(
		private global: GlobalService,
		private router: Router,
		private service: SubscriptionOrderingService,
		private alertService: AlertService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		if (this.global.role !== 'USER') this.router.navigate(['/login']);

		this.service.findAll().subscribe({
			next: (res: any) => this.orderings = res.data,
			error: (e: any) => this.alertService.showAlertMessage(e.error.message)
		})
	}

}
