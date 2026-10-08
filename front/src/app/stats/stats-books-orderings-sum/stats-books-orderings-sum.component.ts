import {Component, Input, OnInit} from '@angular/core';
import {StatsService} from "../stats.service";
import {ChartComponent} from "ng-apexcharts";

@Component({
	selector: 'app-stats-books-orderings-sum',
	imports: [
		ChartComponent
	],
	templateUrl: './stats-books-orderings-sum.component.html',
	standalone: true,
})

export class StatsBooksOrderingsSumComponent implements OnInit{

	@Input() colors: any[] = [];

	chartOptions: any;

	names: any[] = [];
	values: any[] = [];

	constructor(
		private statsService: StatsService,
	) {
	}

	ngOnInit(): void {
		this.statsService.booksOrderingsSum().subscribe({
			next: (res: any) => {
				this.names = res.data.names;
				this.values = res.data.values;
				this.draw();
			},
			error: (e: any) => console.log(e.error)
		})
	}

	draw() {
		const forecast = this.getForecast(this.values);

		// Копируем категории и данные
		let categories = [...this.names];
		let profitSeries: number[] = [...this.values]; // фактическая прибыль
		let forecastSeries: (number | null)[] = new Array(this.values.length - 1).fill(null); // прогнозная линия

		if (forecast !== null) {
			const nextMonth = this.getNextMonth(this.names[this.names.length - 1]);
			categories.push(nextMonth);
			forecastSeries.push(this.values[this.values.length - 1]); // соединение с последней фактической точкой
			forecastSeries.push(forecast); // прогнозная точка
		}

		this.chartOptions = {
			series: [
				{ name: 'Прибыль', data: profitSeries }, // основная линия
				{ name: 'Прогноз', data: forecastSeries } // пунктирная линия прогноза
			],
			chart: { height: 400, type: 'line', zoom: { enabled: false }, toolbar: { show: false } },
			colors: [
				'#008FFB', // цвет основной линии "Прибыль" (синий)
				'#FF4560'  // цвет пунктирной линии "Прогноз" (красный)
			],
			stroke: { curve: 'straight', width: [3, 3], dashArray: [0, 12] }, // 0 = сплошная, 12 = пунктир
			markers: {
				size: 5,
				discrete: [
					{
						seriesIndex: 1, // серия "Прогноз"
						dataPointIndex: forecastSeries.length - 1,
						fillColor: '#FF4560',   // цвет маркера прогнозной точки
						strokeColor: '#fff',    // обводка маркера
						size: 6
					}
				]
			},
			dataLabels: { enabled: false },
			grid: { row: { colors: ['#f3f3f3', 'transparent'], opacity: 0.5 } }, // цвета полос сетки
			xaxis: { categories, labels: { rotate: -45 }, tickPlacement: 'between' },
			annotations: [
				{
					x: categories[categories.length - 1],
					y: forecast,
					marker: { size: 6, fillColor: '#FF4560', strokeColor: '#fff', radius: 2 }, // маркер на прогнозной точке
					label: {
						borderColor: '#FF4560',      // рамка подписи
						offsetY: -10,
						style: { color: '#fff', background: '#FF4560', fontSize: '12px' }, // фон и цвет текста подписи
						text: 'Прогноз' // подпись на прогнозной точке
					}
				}
			],
			legend: { position: 'top', horizontalAlign: 'center' }
		};
	}

	getForecast(values: number[]): number | null {
		if (values.length === 0) return null;
		const lastN = values.slice(-3);
		const sum = lastN.reduce((a, b) => a + b, 0);
		return +(sum / lastN.length).toFixed(2);
	}

	getNextMonth(last: string): string {
		let [year, month] = last.split('-').map(Number);
		month += 1;
		if (month > 12) {
			month = 1;
			year += 1;
		}
		return `${year}-${month.toString().padStart(2, '0')}`;
	}

	// draw() {
	// 	this.chartOptions = {
	// 		series: [
	// 			{
	// 				name: "Прибыль",
	// 				data: this.values
	// 			}
	// 		],
	// 		chart: {
	// 			height: 400,
	// 			type: "line",
	// 			zoom: {
	// 				enabled: false
	// 			}
	// 		},
	// 		dataLabels: {
	// 			enabled: false
	// 		},
	// 		stroke: {
	// 			curve: "straight"
	// 		},
	// 		grid: {
	// 			row: {
	// 				colors: ["#f3f3f3", "transparent"], // takes an array which will be repeated on columns
	// 				opacity: 0.5
	// 			}
	// 		},
	// 		xaxis: {
	// 			categories: this.names
	// 		}
	// 	};
	// }

}
