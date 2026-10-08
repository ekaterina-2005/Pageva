import {Component} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";

@Component({
	selector: 'app-reg',
	standalone: true,
	imports: [
		FormsModule,
		ReactiveFormsModule,
	],
	templateUrl: './reg.component.html',
})
export class RegComponent {

	regForm = new FormGroup({
		username: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		password: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255), Validators.pattern(/^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*()_+=.,|])[A-Za-z\d!@#$%^&*()_+=.,|]{8,}$/)]),
		fio: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		email: new FormControl("", [Validators.required, Validators.email, Validators.minLength(1), Validators.maxLength(255)]),
	})

	constructor(
		private authService: AuthService,
	) {
	}

	regFormSubmit() {
		this.authService.reg(this.regForm.value);
	}
}
