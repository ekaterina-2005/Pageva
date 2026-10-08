import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BehaviorSubject} from "rxjs";
import {GlobalService} from "./global.service";

@Injectable({
	providedIn: 'root'
})

export class EnumService {

	enumSubject = new BehaviorSubject<any>({
		roles: [],
		bookAges: [],
		bookTypes: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/enums';
	}

	getRoles() {
		this.http.get(
			this.url + '/roles',
		).subscribe({
			next: (res: any) => this.enumSubject.next({
				...this.enumSubject.value,
				roles: res.data,
			}),
			error: (e: any) => console.log(e.error),
		})
	}

	getBookAges() {
		this.http.get(
			this.url + '/bookAges',
		).subscribe({
			next: (res: any) => this.enumSubject.next({
				...this.enumSubject.value,
				bookAges: res.data,
			}),
			error: (e: any) => console.log(e.error),
		})
	}

	getBookTypes() {
		this.http.get(
			this.url + '/book/types',
		).subscribe({
			next: (res: any) => this.enumSubject.next({
				...this.enumSubject.value,
				bookTypes: res.data,
			}),
			error: (e: any) => console.log(e.error),
		})
	}

}
