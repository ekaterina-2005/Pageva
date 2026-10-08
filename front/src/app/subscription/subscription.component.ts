import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../global.service";
import {SubscriptionService} from "./subscription.service";
import {NgIf} from "@angular/common";
import {NavigateDirective} from "../navigate.directive";
import {ReactiveFormsModule} from "@angular/forms";

@Component({
	selector: 'app-subscription',
	imports: [
		NavigateDirective,
		NgIf,
		ReactiveFormsModule,
	],
	templateUrl: './subscription.component.html',
	standalone: true,
})

export class SubscriptionComponent implements OnInit {

	subscriptions: any[] = [];

	get subscriptionsSorted() {
		let res = this.subscriptions;

		return res;
	}

	constructor(
		private global: GlobalService,
		private service: SubscriptionService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		this.service.subscriptionSubject.subscribe(value => {
			this.subscriptions = value.subscriptions;
		})
		this.service.findAll();
	}

}
