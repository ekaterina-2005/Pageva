import {Component, Input, OnInit} from '@angular/core';
import {BookCommentService} from "../../book-comment.service";
import {GlobalService} from "../../../global.service";
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";

@Component({
	selector: 'app-book-page-comment',
	imports: [
		NgIf,
		FormsModule,
		ReactiveFormsModule
	],
	templateUrl: './book-page-comment.component.html',
	standalone: true
})

export class BookPageCommentComponent implements OnInit {

	@Input() comment: any;

	commentFormGroup = new FormGroup({
		score: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(10)]),
		text: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(5000)]),
	})

	constructor(
		private commentService: BookCommentService,
		private global: GlobalService,
	) {
	}

	get userid() {
		return this.global.userid;
	}

	ngOnInit(): void {
		this.commentFormGroup.setValue({
			score: this.comment.score,
			text: this.comment.text,
		})
	}

	update() {
		this.commentService.update(this.comment.id, this.commentFormGroup.value).subscribe({
			next: (res: any) => {
				this.comment = res.data
			},
			error: (e: any) => this.global.error(e),
		})
	}

}
