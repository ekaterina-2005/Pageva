import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {GlobalService} from "../global.service";
import {AlertService} from "../alert/alert.service";

@Injectable({
	providedIn: 'root'
})

export class AuthService {

	constructor(
		private http: HttpClient,
		private router: Router,
		private global: GlobalService,
		private alert: AlertService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/users'
	}

	private error(e: any) {
		console.log(e.error);
		if (e.status === 0) this.alert.showAlertMessage("Сервер не работает");
		else this.alert.showAlertMessage(e.error.message)
	}

	login(user: any) {
		this.http.post(
			this.url + '/login',
			"",
			{headers: {'Authorization': 'Basic ' + btoa(user.username + ":" + user.password)}}
		).subscribe({
			next: (res: any) => {
				this.global.set(res.data);
				this.router.navigate(['/']);
			},
			error: (e: any) => {
				console.log(e.error);
				if (e.status === 0) this.alert.showAlertMessage("Сервер не работает");
				else this.alert.showAlertMessage(e.error.data)
			}
		})
	}

	reg(user: any) {
		this.http.post(
			this.url,
			user,
		).subscribe({
			next: () => this.login(user),
			error: (e: any) => this.error(e)
		})
	}

	find() {
		if (this.global.token === "") return;

		this.http.get(
			this.url,
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) => this.global.role = res.data.role,
			error: () => this.global.clear()
		});
	}

	logout() {
		this.global.clear();
		this.router.navigate(['/login'])
	}
}
