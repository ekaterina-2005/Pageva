import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {AlertService} from "../alert/alert.service";

@Injectable({
	providedIn: 'root'
})

export class DiscountService {

	discountSubject = new BehaviorSubject<any>({
		discounts: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private alert: AlertService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/discounts'
	}

	private error(e: any) {
		console.log(e.error);
		this.alert.showAlertMessage(e.error.message);
	}

	findAll() {
		this.http.get(
			this.url,
		).subscribe({
			next: (res: any) => this.discountSubject.next({
				...this.discountSubject.value,
				discounts: res.data,
			}),
			error: (e: any) => this.error(e),
		})
	}

	save(discount: any) {
		this.http.post(
			this.url,
			JSON.stringify(discount),
			{headers: this.global.headersJsonToken}
		).subscribe({
			next: (res: any) => this.discountSubject.next({
				...this.discountSubject.value,
				discounts: [res.data, ...this.discountSubject.value.discounts],
			}),
			error: (e: any) => this.error(e),
		})
	}

	update(id: number, discount: any) {
		this.http.put(
			this.url + `/${id}`,
			JSON.stringify(discount),
			{headers: this.global.headersJsonToken}
		).subscribe({
			next: (res: any) => {
				let discounts = this.discountSubject.value.discounts;
				discounts = discounts.map((i: any) => i.id === id ? res.data : i);
				this.discountSubject.next({
					...this.discountSubject.value,
					discounts: discounts,
				})
			},
			error: (e: any) => this.error(e),
		})
	}

	delete(id: number) {
		this.http.delete(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		).subscribe({
			next: () => {
				let discounts = this.discountSubject.value.discounts;
				discounts = discounts.filter((i: any) => i.id !== id);
				this.discountSubject.next({
					...this.discountSubject.value,
					discounts: discounts,
				})
			},
			error: (e: any) => this.error(e),
		})
	}

}
