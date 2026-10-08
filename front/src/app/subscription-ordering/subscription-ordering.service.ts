import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";

@Injectable({
	providedIn: 'root'
})

export class SubscriptionOrderingService {

	constructor(
		private http: HttpClient,
		private global: GlobalService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/subscriptions/orderings'
	}

	findAll() {
		return this.http.get(
			this.url + '/all',
			{headers: this.global.headersToken}
		)
	}

	active() {
		return this.http.get(
			this.url + '/active',
			{headers: this.global.headersToken}
		)
	}

	save(ordering: any, subscriptionId: number) {
		return this.http.post(
			this.url,
			JSON.stringify(ordering),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					subscriptionId: subscriptionId,
				})
			}
		)
	}

}
