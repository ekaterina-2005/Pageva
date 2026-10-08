import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {AccountService} from "./account.service";
import {AlertService} from "../alert/alert.service";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
	selector: 'app-account',
	imports: [
		FormsModule,
		ReactiveFormsModule
	],
	templateUrl: './account.component.html',
	standalone: true
})

export class AccountComponent implements OnInit {

	user: any;

	userFormGroup = new FormGroup({
		fio: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		date: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		email: new FormControl("", [Validators.required, Validators.email, Validators.minLength(1), Validators.maxLength(255)]),
	})

	constructor(
		private router: Router,
		private global: GlobalService,
		private accountService: AccountService,
		private alert: AlertService,
	) {
	}

	ngOnInit(): void {
		if (this.global.role === 'NOT') this.router.navigate(['/login']);

		this.accountService.find().subscribe({
			next: (res: any) => {
				this.user = res.data

				this.userFormGroup.setValue({
					fio: res.data.fio,
					date: res.data.date,
					email: res.data.email,
				})
			},
			error: (e: any) => {
				console.log(e.error)
				if (e.error.code === 404) {
					this.router.navigate(['/error'], {queryParams: {message: e.error.message}});
				} else {
					this.router.navigate(['/login']);
				}
			}
		})
	}

	updateImg(event: any) {
		this.accountService.updateImg(event.target.files).subscribe({
			next: (res: any) => this.user = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	update() {
		this.accountService.update(this.userFormGroup.value).subscribe({
			next: (res: any) => {
				this.alert.showAlertMessage("Данные обновлены");
				this.user = res.data
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
