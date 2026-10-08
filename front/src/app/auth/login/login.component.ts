import {Component} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../auth.service";

@Component({
	selector: 'app-login',
	standalone: true,
	imports: [
		ReactiveFormsModule,
		FormsModule,
	],
	templateUrl: './login.component.html',
})

export class LoginComponent {

	loginForm = new FormGroup({
		username: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
		password: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(255)]),
	})

	constructor(
		private authService: AuthService,
	) {
	}

	loginFormSubmit() {
		this.authService.login(this.loginForm.value);
	}
}
