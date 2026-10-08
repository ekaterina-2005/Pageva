import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {NgApexchartsModule} from "ng-apexcharts";
import {FormsModule} from "@angular/forms";
import {GlobalService} from "../global.service";
import {
	StatsBooksCategoriesQuantityComponent
} from "./stats-books-categories-quantity/stats-books-categories-quantity.component";
import {StatsBooksAgeComponent} from "./stats-books-age/stats-books-age.component";
import {StatsBooksOrderingsSizeComponent} from "./stats-books-orderings-size/stats-books-orderings-size.component";
import {StatsBooksScoreUpComponent} from "./stats-books-score-up/stats-books-score-up.component";
import {StatsBooksScoreDownComponent} from "./stats-books-score-down/stats-books-score-down.component";
import {StatsOrderingsStatusComponent} from "./stats-orderings-status/stats-orderings-status.component";
import {StatsBooksCountComponent} from "./stats-books-count/stats-books-count.component";
import {StatsBooksOrderingsSumComponent} from "./stats-books-orderings-sum/stats-books-orderings-sum.component";
import {
	StatsBooksRatioTimeScoreScatterComponent
} from "./stats-books-ratio-time-score-scatter/stats-books-ratio-time-score-scatter.component";
import {
	StatsBooksRatioTimeScoreBubbleComponent
} from "./stats-books-ratio-time-score-bubble/stats-books-ratio-time-score-bubble.component";
import {EnumService} from "../enum.service";
import {CategoryService} from "../category/category.service";
import {KeyValuePipe, NgForOf, NgIf} from "@angular/common";
import { StatsKpiComponent } from "./stats-kpi/stats-kpi.component";
import {ChartReadyService} from "./chart-ready.service";
import { ApexInterceptorService } from "./apex-interceptor.service";
import html2canvas from "html2canvas";
import jsPDF from "jspdf";

@Component({
	selector: 'app-stats',
	standalone: true,
	imports: [
		NgApexchartsModule,
		FormsModule,
		StatsBooksCategoriesQuantityComponent,
		StatsBooksAgeComponent,
		StatsBooksOrderingsSizeComponent,
		StatsBooksScoreUpComponent,
		StatsBooksScoreDownComponent,
		StatsOrderingsStatusComponent,
		StatsBooksCountComponent,
		StatsBooksOrderingsSumComponent,
		StatsBooksRatioTimeScoreScatterComponent,
		// StatsBooksRatioTimeScoreBubbleComponent,
		NgForOf,
		KeyValuePipe,
		StatsKpiComponent,
		NgIf,
	],
	templateUrl: './stats.component.html',
})

export class StatsComponent implements OnInit {

	colors: any[] = [
		"#D9230F", // красный
		"#D94F1F", // оранжево-красный
		"#E89A35", // жёлто-оранжевый
		"#FFD700", // жёлтый
		"#8CD93F", // лаймовый
		"#00C853", // ярко-зелёный
		"#00B8A9", // бирюзовый
		"#0286B4", // фирменный голубой
		"#2962FF", // ярко-синий
		"#3D5AFE", // индиго
		"#7C4DFF", // фиолетовый
		"#8A4BD9", // пурпурный
		"#D500F9"  // фуксия
	];

	// colors: any[] = [
	// 	"#008FFB",
	// 	"#00E396",
	// 	"#FEB019",
	// 	"#FF4560",
	// 	"#775DD0",
	// 	"#546E7A",
	// 	"#26a69a",
	// 	"#D10CE8",
	// 	"#a07822",
	// 	"#0029ff",
	// ]

	shuffleArray<T>(array: T[]): T[] {
		const shuffled = [...array];
		for (let i = shuffled.length - 1; i > 0; i--) {
			const j = Math.floor(Math.random() * (i + 1));
			[shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
		}
		return shuffled;
	}

	get colorsShuffle() {
		// return this.shuffleArray(this.colors);
		return this.colors;
	}

	ages: any[] = [];
	age: string = '';

	categories: any[] = [];
	categoryId: string = '';
	showNorms = false;

	constructor(
		private router: Router,
		private global: GlobalService,
		private enumService: EnumService,
		private categoryService: CategoryService
	) {
	}

	ngOnInit(): void {
		if (this.global.role !== 'ADMIN') this.router.navigate(['/login']);

		this.enumService.enumSubject.subscribe(value => {
			this.ages = value.bookAges;
		})
		this.enumService.getBookAges();

		this.categoryService.categorySubject.subscribe(value => {
			this.categories = value.categories
		})
		this.categoryService.findAll();
	}

	private async buildAnalyticsCanvas(): Promise<HTMLCanvasElement | null> {
		const element = document.getElementById('analyticsContent');
		if (!element) return null;

		await new Promise(r => setTimeout(r, 1500));
		await new Promise(r => setTimeout(r, 300));

		// Отключаем AOS
		document.querySelectorAll('[data-aos]').forEach(el => {
			el.removeAttribute('data-aos');
			el.classList.remove('aos-animate');
		});

		return html2canvas(element, {
			scale: 2,
			useCORS: true,
			scrollY: -window.scrollY
		});
	}

	async downloadPDF() {
		await this.downloadReport('PDF');
	}

	async downloadReport(format: 'PDF' | 'CSV' | 'PNG' | 'SVG') {

		if (format === 'CSV') {
			const rows = Array.from(document.querySelectorAll('#analyticsContent .card p.mb-2'))
				.map((el, index) => `${index + 1};"${(el.textContent || '').trim()}"`);
			const csv = 'id;section\n' + rows.join('\n');
			const blob = new Blob([csv], {type: 'text/csv;charset=utf-8;'});
			const url = URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = url;
			a.download = 'analytics.csv';
			a.click();
			URL.revokeObjectURL(url);
			return;
		}

		const canvas = await this.buildAnalyticsCanvas();
		if (!canvas) return;

		if (format === 'PNG') {
			const a = document.createElement('a');
			a.href = canvas.toDataURL('image/png');
			a.download = 'analytics.png';
			a.click();
			return;
		}

		if (format === 'SVG') {
			const pngDataUrl = canvas.toDataURL('image/png');
			const svg = `<svg xmlns="http://www.w3.org/2000/svg" width="${canvas.width}" height="${canvas.height}"><image href="${pngDataUrl}" width="${canvas.width}" height="${canvas.height}" /></svg>`;
			const blob = new Blob([svg], {type: 'image/svg+xml;charset=utf-8'});
			const url = URL.createObjectURL(blob);
			const a = document.createElement('a');
			a.href = url;
			a.download = 'analytics.svg';
			a.click();
			URL.revokeObjectURL(url);
			return;
		}

		// Создаём PDF
		const img = canvas.toDataURL('image/png');
		const pdf = new jsPDF('p', 'mm', 'a4');

		const pageWidth = pdf.internal.pageSize.getWidth();
		const pageHeight = pdf.internal.pageSize.getHeight();
		const width = pageWidth;
		const height = canvas.height * (pageWidth / canvas.width);

		let position = 0;
		let heightLeft = height;

		// Первая страница
		pdf.addImage(img, 'PNG', 0, position, width, height);
		heightLeft -= pageHeight;

		// Остальные страницы
		while (heightLeft > 0) {
			pdf.addPage();
			position = heightLeft - height;
			pdf.addImage(img, 'PNG', 0, position, width, height);
			heightLeft -= pageHeight;
		}

		pdf.save("analytics.pdf");
	}
}
