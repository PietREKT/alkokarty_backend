import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {RxStompService} from "../rx-stomp.service";
import {Router} from "@angular/router";
import {Subscription} from "rxjs";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-create-player',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './create-player.component.html',
  styleUrls: [
    './create-player.component.css',
    '../../styles.css'
  ]
})
export class CreatePlayerComponent implements OnInit, OnDestroy {

  subscriptions: Subscription[] = []
  showError : boolean = false;

  constructor(private rxStomp: RxStompService, private router: Router) {
  }

  createPlayerForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(2)
      ])
    }
  )

  ngOnInit() {
    this.subscriptions.push(this.rxStomp.watch('/user/queue/reply').subscribe((msg) => {
        if (msg.body.startsWith("Token: ")) {
          window.sessionStorage.setItem("cardsToken", msg.body.substring(7));
          this.unsubscribe();
          this.router.navigate(['roomChoice']);
        }
      })
    )
  }

  onSubmit() {
    let username = this.createPlayerForm.controls.name.value;
    if (!this.createPlayerForm.controls.name.invalid) {
      this.showError = false;
      this.rxStomp.publish({destination: '/app/users/addUser', body: username as string})
    } else {
      this.showError = true;
    }
  }



  private unsubscribe() {
      for(let sub of this.subscriptions){
        sub.unsubscribe();
      }
  }

  ngOnDestroy(): void {
    this.unsubscribe();
  }

  onInputClick() {
    this.showError = false;
  }
}
