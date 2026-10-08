import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {Router} from "@angular/router";

@Injectable({
	providedIn: 'root'
})

export class BookCommentService {

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private router: Router,
	) {
	}

	private get url() {
		return this.global.backendURL + '/books/comments'
	}

	save(comment: any, bookId: number) {
		return this.http.post(
			this.url,
			JSON.stringify(comment),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({bookId: bookId})
			}
		)
	}

	update(id: number, comment: any) {
		return this.http.put(
			this.url + `/${id}`,
			JSON.stringify(comment),
			{headers: this.global.headersJsonToken}
		)
	}

}
