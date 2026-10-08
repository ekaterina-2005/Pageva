import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {Router} from "@angular/router";

@Injectable({
	providedIn: 'root'
})

export class SubscriptionService {

	subscriptionSubject = new BehaviorSubject<any>({
		subscriptions: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private router: Router,
	) {
	}

	private get url() {
		return this.global.backendURL + '/subscriptions'
	}

	private page(id: number) {
		this.router.navigate(['/subscription'], {queryParams: {id: id}})
	}

	findAll() {
		this.http.get(
			this.url,
		).subscribe({
			next: (res: any) => this.subscriptionSubject.next({
				...this.subscriptionSubject.value,
				subscriptions: res.data,
			}),
			error: (e: any) => this.global.error(e),
		})
	}

	find(id: number) {
		return this.http.get(
			this.url + `/${id}`,
		)
	}

	save(subscription: any) {
		this.http.post(
			this.url,
			JSON.stringify(subscription),
			{headers: this.global.headersJsonToken}
		).subscribe({
			next: (res: any) => this.page(res.data.id),
			error: (e: any) => this.global.error(e),
		})
	}

	update(id: number, subscription: any) {
		this.http.put(
			this.url + `/${id}`,
			JSON.stringify(subscription),
			{headers: this.global.headersJsonToken}
		).subscribe({
			next: (res: any) => this.page(res.data.id),
			error: (e: any) => this.global.error(e),
		})
	}

	delete(id: number) {
		this.http.delete(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		).subscribe({
			next: () => this.router.navigate(['/subscriptions']),
			error: (e: any) => this.global.error(e),
		})
	}

}
