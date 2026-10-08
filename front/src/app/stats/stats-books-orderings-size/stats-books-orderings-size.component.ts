import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {ChartComponent} from "ng-apexcharts";
import {StatsService} from "../stats.service";

@Component({
	selector: 'app-stats-books-orderings-size',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-books-orderings-size.component.html',
	standalone: true,
})

export class StatsBooksOrderingsSizeComponent implements OnInit, OnChanges {

	@Input() age: string = '';
	@Input() categoryId: string = '';

	@Input() colors: any[] = [];

	chartOptions: any;

	names: any[] = [];
	values: any[] = [];

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.booksOrderingsSize(this.age, this.categoryId).subscribe({
			next: (res: any) => {
				this.names = res.data.names;
				this.values = res.data.values;
				this.draw();
			},
			error: (e: any) => console.log(e.error)
		})
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['age'] || changes['categoryId']) {
			this.statsService.booksOrderingsSize(this.age, this.categoryId).subscribe({
				next: (res: any) => {
					this.names = res.data.names;
					this.values = res.data.values;
					this.draw();
				},
				error: (e: any) => console.log(e.error)
			})
		}
	}

	draw() {
		this.chartOptions = {
			series: [
				{
					data: this.values,
				}
			],
			chart: {
				type: "bar",
				height: 400
			},
			plotOptions: {
				bar: {
					barHeight: "100%",
					distributed: true,
					horizontal: true,
					dataLabels: {
						position: "bottom"
					}
				}
			},
			colors: this.colors,
			// dataLabels: {
			// 	enabled: true,
			// 	textAnchor: "start",
			// 	style: {
			// 		colors: ["#000"]
			// 	},
			// 	formatter: function (val: any, opt: any) {
			// 		return opt.w.globals.labels[opt.dataPointIndex] + ":  " + val;
			// 	},
			// 	offsetX: 0,
			// 	dropShadow: {
			// 		enabled: false
			// 	}
			// },
			dataLabels: {
				enabled: true,
				textAnchor: "start",
				style: {
					colors: ["#000"],
					fontSize: "20px"
				},
				formatter: function (val: any, opt: any) {
					const pad = '\u00A0'.repeat(4);
					return pad + opt.w.globals.labels[opt.dataPointIndex] + ": " + val;
				},
				dropShadow: {
					enabled: false
				}
			},
			stroke: {
				width: 1,
				colors: ["#fff"]
			},
			xaxis: {
				categories: this.names,
			},
			yaxis: {
				labels: {
					show: false
				}
			},
			tooltip: {
				theme: "dark",
				x: {
					show: false
				},
				y: {
					title: {
						formatter: function () {
							return "";
						}
					}
				}
			}
		};
	}

}
