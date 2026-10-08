import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {BehaviorSubject} from "rxjs";

@Injectable({
	providedIn: 'root'
})

export class OrderingService {

	orderingsSubject = new BehaviorSubject<any>({
		orderings: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/books/orderings'
	}

	findAll() {
		this.http.get(
			this.url,
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) => this.orderingsSubject.next({
				...this.orderingsSubject.value,
				orderings: res.data,
			}),
			error: (e: any) => this.global.error(e),
		})
	}

	booked(id: number) {
		this.http.get(
			this.url + `/${id}/booked`,
			{headers: this.global.headersToken}
		).subscribe({
			next: () => this.findAll(),
			error: (e: any) => this.global.error(e),
		})
	}

	rejected(id: number) {
		this.http.get(
			this.url + `/${id}/rejected`,
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) => {
				let orderings = this.orderingsSubject.value.orderings;
				orderings = orderings.map((i: any) => i.id === id ? res.data : i);
				this.orderingsSubject.next({
					...this.orderingsSubject.value,
					orderings: orderings,
				})
			},
			error: (e: any) => this.global.error(e),
		})
	}

	done(id: number) {
		this.http.get(
			this.url + `/${id}/done`,
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) => {
				let orderings = this.orderingsSubject.value.orderings;
				orderings = orderings.map((i: any) => i.id === id ? res.data : i);
				this.orderingsSubject.next({
					...this.orderingsSubject.value,
					orderings: orderings,
				})
			},
			error: (e: any) => this.global.error(e),
		})
	}

	returned(id: number) {
		this.http.get(
			this.url + `/${id}/returned`,
			{headers: this.global.headersToken}
		).subscribe({
			next: (res: any) => {
				let orderings = this.orderingsSubject.value.orderings;
				orderings = orderings.map((i: any) => i.id === id ? res.data : i);
				this.orderingsSubject.next({
					...this.orderingsSubject.value,
					orderings: orderings,
				})
			},
			error: (e: any) => this.global.error(e),
		})
	}

	save(ordering: any, bookId: number, discountId: number) {
		return this.http.post(
			this.url,
			JSON.stringify(ordering),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					bookId: bookId,
					discountId: discountId,
				})
			}
		)
	}

	updateDiscount(id:number, discountId: number) {
		this.http.patch(
			this.url + `/${id}/discount`,
			"",
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					discountId: discountId,
				})
			}
		).subscribe({
			next: (res: any) => {
				let orderings = this.orderingsSubject.value.orderings;
				orderings = orderings.map((i: any) => i.id === id ? res.data : i);
				this.orderingsSubject.next({
					...this.orderingsSubject.value,
					orderings: orderings,
				})
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
