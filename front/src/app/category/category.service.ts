import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {BehaviorSubject} from "rxjs";
import {AlertService} from "../alert/alert.service";

@Injectable({
	providedIn: 'root'
})

export class CategoryService {

	categorySubject = new BehaviorSubject<any>({
		categories: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private alert: AlertService,
	) {
	}

	private get url() {
		return this.global.backendURL + '/categories'
	}

	private error(e: any) {
		console.log(e.error);
		this.alert.showAlertMessage(e.error.message);
	}

	findAll() {
		this.http.get(
			this.url,
		).subscribe({
			next: (res: any) => this.categorySubject.next({
				...this.categorySubject.value,
				categories: res.data,
			}),
			error: (e: any) => this.error(e),
		})
	}

	save(category: any) {
		this.http.post(
			this.url,
			JSON.stringify(category),
			{headers: this.global.headersJsonToken}
		).subscribe({
			next: (res: any) => this.categorySubject.next({
				...this.categorySubject.value,
				categories: [res.data, ...this.categorySubject.value.categories],
			}),
			error: (e: any) => this.error(e),
		})
	}

	update(id: number, category: any) {
		this.http.put(
			this.url + `/${id}`,
			JSON.stringify(category),
			{headers: this.global.headersJsonToken}
		).subscribe({
			next: (res: any) => {
				let categories = this.categorySubject.value.categories;
				categories = categories.map((i: any) => i.id === id ? res.data : i);
				this.categorySubject.next({
					...this.categorySubject.value,
					categories: categories,
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
				let categories = this.categorySubject.value.categories;
				categories = categories.filter((i: any) => i.id !== id);
				this.categorySubject.next({
					...this.categorySubject.value,
					categories: categories,
				})
			},
			error: (e: any) => this.error(e),
		})
	}

}
