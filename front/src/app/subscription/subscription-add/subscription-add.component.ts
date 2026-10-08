import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../../global.service";
import {SubscriptionService} from "../subscription.service";
import {Router} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {NavigateDirective} from "../../navigate.directive";

@Component({
	selector: 'app-subscription-add',
	imports: [
		NavigateDirective,
		ReactiveFormsModule
	],
	templateUrl: './subscription-add.component.html',
	standalone: true,
})

export class SubscriptionAddComponent implements OnInit {

	subscriptionFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		duration: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
		price: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000000)]),
	});

	constructor(
		private router: Router,
		private global: GlobalService,
		private service: SubscriptionService,
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'ADMIN') this.router.navigate(['/login']);
	}

	save() {
		this.service.save(this.subscriptionFormGroup.value)
	}

}
