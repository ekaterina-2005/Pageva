import { Component, Input, OnInit } from '@angular/core';
import { StatsService } from '../stats.service';
import { ChartComponent } from 'ng-apexcharts';

@Component({
	selector: 'app-stats-books-categories-quantity',
	standalone: true,
	imports: [ChartComponent],
	templateUrl: './stats-books-categories-quantity.component.html',
})
export class StatsBooksCategoriesQuantityComponent implements OnInit {

	@Input() colors: string[] = [];

	chartOptions: any;

	names: string[] = [];
	values: number[] = [];

	constructor(private statsService: StatsService) {}

	ngOnInit(): void {
		this.statsService.booksCategoriesQuantity().subscribe({
			next: (res: any) => {
				this.names = res.data.names;
				this.values = res.data.values;
				this.draw();
			},
			error: (e: any) => console.log(e)
		});
	}

	draw(): void {
		this.chartOptions = {
			series: [
				{
					name: 'Количество',
					data: this.values
				}
			],
			chart: {
				type: 'bar',
				height: 400,
				toolbar: { show: false }
			},
			colors: this.colors,
			plotOptions: {
				bar: {
					columnWidth: '45%',
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
				show: false,
				padding: {
					left: 50   // отступ слева
				}
			},
			xaxis: {
				categories: this.names,
				labels: {
					offsetY: 0,
					style: {
						colors: '#000000',   // чёрный текст
						fontSize: '12px'
					}
				}
			},
			yaxis: {
				labels: {
					minWidth: 80,
					maxWidth: 80,
					offsetX: -5,          // сдвиг подписей влево
					style: {
						colors: '#000000', // чёрный текст
						fontSize: '12px'
					}
				}
			},
			tooltip: {
				enabled: true,
				style: {
					fontSize: '12px'
				}
			}
		};
	}

}
