import {Component, OnDestroy, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {AuthService} from "./auth/auth.service";
import AOS from "aos";
import {AlertComponent} from "./alert/alert.component";
import {NavComponent} from "./nav/nav.component";
import {FooterComponent} from "./footer/footer.component";

@Component({
	selector: 'app-root',
	standalone: true,
	imports: [
		RouterOutlet,
		FooterComponent,
		NavComponent,
		AlertComponent,
	],
	templateUrl: './app.component.html',
})

export class AppComponent implements OnInit, OnDestroy {

	intervalCheckUser: any;

	constructor(
		private authService: AuthService,
	) {
	}

	ngOnInit(): void {
		AOS.init();
		this.intervalCheckUser = setInterval(() => {
			this.authService.find();
		}, 5000);
	}

	ngOnDestroy(): void {
		if (this.intervalCheckUser) {
			clearInterval(this.intervalCheckUser);
		}
	}

}
