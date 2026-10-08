import {Component, Input, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {ChartComponent} from "ng-apexcharts";

@Component({
	selector: 'app-stats-books-ratio-time-score2',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-books-ratio-time-score-bubble.component.html',
	standalone: true,
})

export class StatsBooksRatioTimeScoreBubbleComponent implements OnInit {

	@Input() colors: any[] = [];

	chartOptions: any;

	data: any;

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.booksRatioTimeScoreBubble().subscribe({
			next: (res: any) => {
				this.data = res.data;
				console.log(this.data)

				this.draw();
			},
			error: (e: any) => console.log(e.error)
		})
	}

	draw() {
		this.chartOptions = {
			series: this.data,
			chart: {
				height: 400,
				type: "bubble"
			},
			dataLabels: {
				enabled: false
			},
			fill: {
				opacity: 0.8
			},
			xaxis: {
				tickAmount: 12,
				type: "category"
			},
			yaxis: {
				max: 70
			}
		};
	}

}
