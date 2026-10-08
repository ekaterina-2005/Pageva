import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {GlobalService} from "../../global.service";
import {SubscriptionService} from "../subscription.service";
import {NavigateDirective} from "../../navigate.directive";

@Component({
	selector: 'app-subscription-update',
	imports: [
		NavigateDirective,
		ReactiveFormsModule
	],
	templateUrl: './subscription-update.component.html',
	standalone: true,
})

export class SubscriptionUpdateComponent implements OnInit {

	id: number = 0;

	subscriptionFormGroup = new FormGroup({
		name: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		duration: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000)]),
		price: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(1000000)]),
	});

	constructor(
		private router: Router,
		private global: GlobalService,
		private service: SubscriptionService,
		private activatedRoute: ActivatedRoute,
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'ADMIN') this.router.navigate(['/login']);

		this.activatedRoute.queryParams.subscribe(params => {
			this.id = params['id'];
			this.service.find(params['id']).subscribe({
				next: (res: any) => {
					this.subscriptionFormGroup.setValue({
						name: res.data.name,
						duration: res.data.duration,
						price: res.data.price,
					})
				},
				error: (e: any) => {
					console.log(e.error)
					if (e.error.code === 404) {
						this.router.navigate(['/error'], {queryParams: {message: e.error.message}})
					} else {
						this.router.navigate(['/login'])
					}
				}
			})
		})
	}

	update() {
		this.service.update(this.id, this.subscriptionFormGroup.value)
	}

}
