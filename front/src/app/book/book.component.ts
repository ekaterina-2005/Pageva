import {Component, OnInit} from '@angular/core';
import {GlobalService} from "../global.service";
import {BookService} from "./book.service";
import {KeyValuePipe, NgForOf, NgIf} from "@angular/common";
import {NavigateDirective} from "../navigate.directive";
import {CategoryService} from "../category/category.service";
import {FormsModule} from "@angular/forms";
import {EnumService} from "../enum.service";

@Component({
	selector: 'app-book',
	imports: [
		NgIf,
		NavigateDirective,
		FormsModule,
		NgForOf,
		KeyValuePipe
	],
	templateUrl: './book.component.html',
	standalone: true
})

export class BookComponent implements OnInit {

	books: any[] = [];

	filterName: string = '';
	filterAuthor: string = '';
	filterSort: string = '0';

	filterType: string = '';
	filterStatus: string = '';
	filterAge: string = '';
	filterCategoryId: number = 0;

	types: any[] = []
	categories: any[] = []
	ages: any[] = []

	get booksSorted() {
		let res = this.books;

		if (this.role !== 'MANAGER' && this.role !== 'ADMIN') {
			res = res.filter((i: any) => !i.repair);
			res = res.filter((i: any) => i.visibility);
		}

		if (this.filterType) res = res.filter((i: any) => i.type === this.filterType);
		if (this.filterAuthor) res = res.filter((i: any) => i.author.toLowerCase().includes(this.filterAuthor.toLowerCase()));
		if (this.filterCategoryId != 0) res = res.filter((i: any) => i.categoryId == this.filterCategoryId);
		if (this.filterAge) res = res.filter((i: any) => i.age === this.filterAge);

		switch (this.filterStatus) {
			case 'FREE':
				res = res.filter((i: any) => i.status);
				break;
			case 'BUSY':
				res = res.filter((i: any) => !i.status);
				break;
		}

		switch (this.filterSort) {
			case "0":
				res = res.sort((a: any, b: any) => b.id - a.id);
				break;
			case "1":
				res = res.sort((a: any, b: any) => a.price - b.price);
				break;
			case "2":
				res = res.sort((a: any, b: any) => b.price - a.price);
				break;
			case "3":
				res = res.sort((a: any, b: any) => a.page - b.page);
				break;
			case "4":
				res = res.sort((a: any, b: any) => b.page - a.page);
				break;
		}

		return res;
	}

	currentPage = 1;
	pageSize = 6;

	get totalPages(): number {
		return Math.ceil(this.booksSorted.length / this.pageSize);
	}

	get paginatedItems(): any[] {
		const start = (this.currentPage - 1) * this.pageSize;
		return this.booksSorted.slice(start, start + this.pageSize);
	}

	nextPage(): void {
		if (this.currentPage < this.totalPages) {
			this.currentPage++;
		}
	}

	previousPage(): void {
		if (this.currentPage > 1) {
			this.currentPage--;
		}
	}



	constructor(
		private global: GlobalService,
		private bookService: BookService,
		private categoryService: CategoryService,
		private enumService: EnumService,
	) {
	}

	get role() {
		return this.global.role;
	}

	ngOnInit(): void {
		this.bookService.bookSubject.subscribe(value => {
			this.books = value.books;
		})
		this.bookService.findAll();

		this.categoryService.categorySubject.subscribe(value => {
			this.categories = value.categories;
		})
		this.categoryService.findAll();

		this.enumService.enumSubject.subscribe(value => {
			this.ages = value.bookAges;
			this.types = value.bookTypes;
		})
		this.enumService.getBookAges();
		this.enumService.getBookTypes();
	}

}
