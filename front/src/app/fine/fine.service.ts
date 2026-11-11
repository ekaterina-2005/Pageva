import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GlobalService} from "../global.service";

@Injectable({
	providedIn: 'root'
})

export class FineService {

	constructor(
		private http: HttpClient,
		private global: GlobalService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/fines'
	}

	findAll() {
		return this.http.get(
			this.url,
			{headers: this.global.headersToken}
		)
	}

	pay(id: number) {
		return this.http.patch(
			this.url + `/${id}/paid`,
			"",
			{headers: this.global.headersJsonToken}
		)
	}

	cancel(id: number) {
		return this.http.patch(
			this.url + `/${id}/cancel`,
			"",
			{headers: this.global.headersJsonToken}
		)
	}

}
