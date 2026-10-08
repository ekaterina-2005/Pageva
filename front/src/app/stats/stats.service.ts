import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {GlobalService} from "../global.service";
import {Observable} from "rxjs";

@Injectable({
	providedIn: 'root'
})
export class StatsService {

	constructor(
		private http: HttpClient,
		private global: GlobalService
	) {
	}

	private get url() {
		return this.global.backendURL + '/stats'
	}

	booksCategoriesQuantity() {
		return this.http.get(
			this.url + '/books/categories/quantity',
			{headers: this.global.headersToken}
		);
	}

	booksAge(age: string, categoryId: string) {
		return this.http.get(
			this.url + '/books/age',
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({
					age: age,
					categoryId: categoryId,
				})
			}
		);
	}

	booksOrderingsSize(age: string, categoryId: string) {
		return this.http.get(
			this.url + '/books/orderings/size',
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({
					age: age,
					categoryId: categoryId,
				})
			}
		);
	}

	booksScoreUp(age: string, categoryId: string) {
		return this.http.get(
			this.url + '/books/score/up',
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({
					age: age,
					categoryId: categoryId,
				})
			}
		);
	}

	booksScoreDown(age: string, categoryId: string) {
		return this.http.get(
			this.url + '/books/score/down',
			{
				headers: this.global.headersToken,
				params: new HttpParams().appendAll({
					age: age,
					categoryId: categoryId,
				})
			}
		);
	}

	orderingsStatus() {
		return this.http.get(
			this.url + '/books/orderings/status',
			{headers: this.global.headersToken}
		);
	}

	booksCount() {
		return this.http.get(
			this.url + '/books/count',
			{headers: this.global.headersToken}
		);
	}

	booksOrderingsSum() {
		return this.http.get(
			this.url + '/books/orderings/sum',
			{headers: this.global.headersToken}
		);
	}

	booksRatioTimeScoreScatter() {
		return this.http.get(
			this.url + '/books/ratio/time/score/scatter',
			{headers: this.global.headersToken}
		);
	}

	booksRatioTimeScoreBubble() {
		return this.http.get(
			this.url + '/books/ratio/time/score/bubble',
			{headers: this.global.headersToken}
		);
	}

	// Явно возвращаем any — потом в компоненте безопасно парсим
	kpiBooksTotal(): Observable<any> {
		return this.http.get<any>(
			this.url + '/kpi/books-total',
			{ headers: this.global.headersToken }
		);
	}

	kpiBooksTotalInLibrary(): Observable<any> {
		return this.http.get<any>(
			this.url + '/kpi/books-total-in-library',
			{ headers: this.global.headersToken }
		);
	}

	kpiBooksAvgScore(): Observable<any> {
		return this.http.get<any>(
			this.url + '/kpi/books-avg-score',
			{ headers: this.global.headersToken }
		);
	}

	kpiOrderingsMonthly(): Observable<any> {
		return this.http.get<any>(
			this.url + '/kpi/orderings-monthly',
			{ headers: this.global.headersToken }
		);
	}

	kpiIncomeMonthly(): Observable<any> {
		return this.http.get<any>(
			this.url + '/kpi/income-monthly',
			{ headers: this.global.headersToken }
		);
	}
}
