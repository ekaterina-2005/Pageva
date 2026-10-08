import {Injectable} from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class AlertService {
	private alertMessage = new Subject<string>();
	alertMessage$ = this.alertMessage.asObservable();

	showAlertMessage(message: string) {
		this.alertMessage.next(message);
		setTimeout(() => this.alertMessage.next(''), 3000);
	}
}
