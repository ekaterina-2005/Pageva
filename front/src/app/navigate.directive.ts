import {Directive, HostListener, Input} from '@angular/core';
import {NavigationExtras, Router} from "@angular/router";

@Directive({
	selector: '[appNavigate]',
	standalone: true
})
export class NavigateDirective {

	@Input() navigateURL: string = '';
	@Input() queryParams: any;

	constructor(
		private router: Router,
	) {
	}

	@HostListener('click', ['$event'])
	onClick(event: MouseEvent) {
		let URL = '/' + this.navigateURL;
		const navigationExtras: NavigationExtras = {
			queryParams: this.queryParams
		};

		if (event.ctrlKey || event.metaKey) {
			const urlWithParams = this.router.createUrlTree([URL], navigationExtras).toString();
			window.open(urlWithParams, '_blank');
			event.preventDefault();
		} else {
			this.router.navigate([URL], navigationExtras);
		}
	}

}
