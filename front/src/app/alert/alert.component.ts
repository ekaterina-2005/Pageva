import {Component, OnInit} from '@angular/core';
import {AlertService} from "./alert.service";
import {NgIf} from "@angular/common";

@Component({
	selector: 'app-alert',
	standalone: true,
	imports: [
		NgIf
	],
	templateUrl: './alert.component.html',
})
export class AlertComponent implements OnInit {
	message: string = '';

	constructor(
		private alertService: AlertService
	) {
	}

	ngOnInit() {
		this.alertService.alertMessage$.subscribe(message => {
			this.message = message;
		});
	}
}
