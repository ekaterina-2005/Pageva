import {Component, Input, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {ChartComponent} from "ng-apexcharts";

@Component({
	selector: 'app-stats-books-count',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-books-count.component.html',
	standalone: true,
})

export class StatsBooksCountComponent implements OnInit{

	@Input() colors: any[] = [];

	chartOptions: any;

	names: any[] = [];
	values: any[] = [];

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.booksCount().subscribe({
			next: (res: any) => {
				this.names = res.data.names;
				this.values = res.data.values;
				this.draw();
			},
			error: (e: any) => console.log(e.error)
		})
	}

	draw() {
		this.chartOptions = {
			series: [
				{
					name: "Сколько раз брали в аренду",
					data: this.values
				}
			],
			chart: {
				height: 400,
				type: "bar",
			},
			colors: this.colors,
			plotOptions: {
				bar: {
					columnWidth: "45%",
					distributed: true
				}
			},
			dataLabels: {
				enabled: false
			},
			legend: {
				show: false
			},
			grid: {
				show: false
			},
			xaxis: {
				categories: this.names,
				labels: {
					style: {
						colors:this.colors,
						fontSize: "12px"
					}
				}
			}
		};
	}

}
