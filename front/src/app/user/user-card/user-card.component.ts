import {Component, Input, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {KeyValuePipe, NgForOf, NgIf} from "@angular/common";
import {UserService} from "../user.service";
import {EnumService} from "../../enum.service";
import {GlobalService} from "../../global.service";

@Component({
	selector: 'app-user-card',
	standalone: true,
	imports: [
		FormsModule,
		NgForOf,
		KeyValuePipe,
		NgIf
	],
	templateUrl: './user-card.component.html',
})
export class UserCardComponent implements OnInit {

	@Input() user: any;

	roles: any[] = [];

	constructor(
		private userService: UserService,
		private enumService: EnumService,
		private global: GlobalService,
	) {
	}

	ngOnInit() {
		this.enumService.enumSubject.subscribe(value => {
			this.roles = value.roles;
		})
	}

	updateRole() {
		this.userService.updateRole(this.user.id, this.user.role);
	}

	delete() {
		this.userService.delete(this.user.id);
	}

	lock() {
		this.userService.updateLock(this.user.id).subscribe({
			next: () => this.user.lock = !this.user.lock,
			error: (e: any) => this.global.error(e),
		})
	}
}
