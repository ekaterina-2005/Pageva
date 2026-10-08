import {Component, OnInit} from '@angular/core';
import {BookService} from "../book/book.service";
import {NavigateDirective} from "../navigate.directive";
import {NgIf} from "@angular/common";

@Component({
	selector: 'app-main',
	standalone: true,
	imports: [
		NavigateDirective,
		NgIf
	],
	templateUrl: './main.component.html',
})

export class MainComponent implements OnInit {

	books: any[] = [];

	get booksSorted() {
		return this.books.slice(0, 3);
	}

	constructor(
		private bookService: BookService,
	) {
	}

	ngOnInit(): void {
		this.bookService.bookSubject.subscribe(value => {
			this.books = value.books;
		})
		this.bookService.findAll();
	}

}
