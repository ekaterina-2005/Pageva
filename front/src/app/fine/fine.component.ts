import {Component, OnInit} from '@angular/core';
import {DecimalPipe, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {NavigateDirective} from "../navigate.directive";
import {GlobalService} from "../global.service";
import {FineService} from "./fine.service";

@Component({
	selector: 'app-fine',
	imports: [
		NavigateDirective,
		NgIf,
		DecimalPipe
	],
	templateUrl: './fine.component.html',
	standalone: true
})

export class FineComponent implements OnInit {

	fines: any[] = [];

	constructor(
		private router: Router,
		private global: GlobalService,
		private fineService: FineService,
	) {
	}

	get role() {
		return this.global.role;
	}

	get unpaidSum(): number {
		return this.fines
			.filter((i: any) => i.status === 'UNPAID')
			.reduce((sum: number, i: any) => sum + Number(i.amount), 0);
	}

	ngOnInit(): void {
		if (this.role !== 'MANAGER' && this.role !== 'USER') this.router.navigate(['/login']);

		this.fineService.findAll().subscribe({
			next: (res: any) => this.fines = res.data,
			error: (e: any) => this.global.error(e),
		})
	}

	pay(id: number) {
		this.fineService.pay(id).subscribe({
			next: (res: any) => this.replace(res.data),
			error: (e: any) => this.global.error(e),
		})
	}

	cancel(id: number) {
		this.fineService.cancel(id).subscribe({
			next: (res: any) => this.replace(res.data),
			error: (e: any) => this.global.error(e),
		})
	}

	private replace(fine: any) {
		this.fines = this.fines.map((i: any) => i.id === fine.id ? fine : i);
	}

}
