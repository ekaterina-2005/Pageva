import {Component, Input, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {ChartComponent} from "ng-apexcharts";

@Component({
	selector: 'app-stats-books-ratio-time-score1',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-books-ratio-time-score-scatter.component.html',
	standalone: true,
})

export class StatsBooksRatioTimeScoreScatterComponent implements OnInit {

	@Input() colors: any[] = [];

	chartOptions: any;

	data:any;

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.booksRatioTimeScoreScatter().subscribe({
			next: (res: any) => {
				this.data = res.data;
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
				type: 'scatter',
				zoom: {
					enabled: true,
					type: 'xy'
				}
			},
			xaxis: {
				tickAmount: 10,
				labels: {
					formatter: function(val:any) {
						return parseFloat(val).toFixed(1)
					}
				}
			},
			yaxis: {
				tickAmount: 7
			}
		};
	}

}
