import {Injectable} from '@angular/core';
import {BehaviorSubject} from "rxjs";
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {Router} from "@angular/router";

@Injectable({
	providedIn: 'root'
})

export class BookService {

	bookSubject = new BehaviorSubject<any>({
		books: [],
	})

	constructor(
		private http: HttpClient,
		private global: GlobalService,
		private router: Router,
	) {
	}

	private get url() {
		return this.global.backendURL + '/books'
	}

	private page(id: number) {
		this.router.navigate(['/book'], {queryParams: {id: id}})
	}

	findAll() {
		this.http.get(
			this.url,
		).subscribe({
			next: (res: any) => this.bookSubject.next({
				...this.bookSubject.value,
				books: res.data,
			}),
			error: (e: any) => this.global.error(e),
		})
	}

	find(id: number) {
		return this.http.get(
			this.url + `/${id}`,
		)
	}

	save(book: any, categoryId: number, age: string, type: string, img: any, file: any) {
		this.http.post(
			this.url,
			JSON.stringify(book),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					categoryId: categoryId,
					age: age,
					type: type,
				})
			}
		).subscribe({
			next: (res: any) => this.updateImg(res.data.id, img, file),
			error: (e: any) => this.global.error(e),
		})
	}

	update(id: number, book: any, categoryId: number, age: string, type: string, img: any, file: any) {
		this.http.put(
			this.url + `/${id}`,
			JSON.stringify(book),
			{
				headers: this.global.headersJsonToken,
				params: new HttpParams().appendAll({
					categoryId: categoryId,
					age: age,
					type: type,
				})
			}
		).subscribe({
			next: (res: any) => {
				if (img !== null) this.updateImg(res.data.id, img, file)
				else if (file !== null) this.updateFile(res.data.id, file)
				else this.page(res.data.id)
			},
			error: (e: any) => this.global.error(e),
		})
	}

	private updateImg(id: number, files: any, file: any) {
		let formData = new FormData()
		for (let i = 0; i < files.length; i++) {
			formData.append('file', files[i])
		}
		this.http.patch(
			this.url + `/${id}/img`,
			formData,
			{headers: this.global.headersMultipartToken}
		).subscribe({
			next: (res: any) => {
				if (file !== null) this.updateFile(res.data.id, file)
				else this.page(res.data.id)
			},
			error: (e: any) => this.global.error(e),
		})
	}

	private updateFile(id: number, files: any) {
		let formData = new FormData()
		for (let i = 0; i < files.length; i++) {
			formData.append('file', files[i])
		}
		this.http.patch(
			this.url + `/${id}/file`,
			formData,
			{headers: this.global.headersMultipartToken}
		).subscribe({
			next: (res: any) => this.page(res.data.id),
			error: (e: any) => this.global.error(e),
		})
	}

	updateRepair(id: number) {
		return this.http.patch(
			this.url + `/${id}/repair`,
			"",
			{headers: this.global.headersToken}
		)
	}

	visibility(id: number) {
		return this.http.patch(
			this.url + `/${id}/visibility`,
			"",
			{headers: this.global.headersToken}
		)
	}

	delete(id: number) {
		this.http.delete(
			this.url + `/${id}`,
			{headers: this.global.headersToken}
		).subscribe({
			next: () => this.router.navigate(['/books']),
			error: (e: any) => this.global.error(e),
		})
	}

}
