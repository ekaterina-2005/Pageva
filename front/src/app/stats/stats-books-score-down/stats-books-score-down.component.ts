import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {StatsService} from "../stats.service";

@Component({
	selector: 'app-stats-books-score-down',
	imports: [],
	templateUrl: './stats-books-score-down.component.html',
	standalone: true,
})

export class StatsBooksScoreDownComponent implements OnInit, OnChanges {

	@Input() age: string = '';
	@Input() categoryId: string = '';

	books: any[] = [];

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.booksScoreDown(this.age, this.categoryId).subscribe({
			next: (res: any) => {
				this.books = res.data;
			},
			error: (e: any) => console.log(e.error)
		})
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['age'] || changes['categoryId']) {
			this.statsService.booksScoreDown(this.age, this.categoryId).subscribe({
				next: (res: any) => {
					this.books = res.data;
				},
				error: (e: any) => console.log(e.error)
			})
		}
	}

}
