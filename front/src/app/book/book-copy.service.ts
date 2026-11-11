import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";

@Injectable({
	providedIn: 'root'
})

export class BookCopyService {

	constructor(
		private http: HttpClient,
		private global: GlobalService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/books'
	}

	findAll(bookId: number) {
		return this.http.get(
			this.url + `/${bookId}/copies`,
			{headers: this.global.headersToken}
		)
	}

	save(bookId: number, count: number) {
		return this.http.post(
			this.url + `/${bookId}/copies`,
			"",
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					count: count,
				})
			}
		)
	}

	updateStatus(id: number, status: string) {
		return this.http.patch(
			this.url + `/copies/${id}/status`,
			"",
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					status: status,
				})
			}
		)
	}

	delete(id: number) {
		return this.http.delete(
			this.url + `/copies/${id}`,
			{headers: this.global.headersToken}
		)
	}

}
