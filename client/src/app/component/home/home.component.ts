import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  constructor(private router: Router){}

  searchCharacter(form: NgForm){
    const characterName = form.value.characterName;
    this.router.navigate(['/characters'], {queryParams: {'nameStartsWith' : characterName}});
  }
}
