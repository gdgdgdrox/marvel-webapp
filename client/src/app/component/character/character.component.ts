import { Component, OnInit } from '@angular/core';
import { MarvelService } from '../../service/marvel.service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MarvelCharacter } from '../../interface/marvelcharacter';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-character',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './character.component.html',
  styleUrl: './character.component.css'
})
export class CharacterComponent implements OnInit{
  characters: MarvelCharacter[] = [];
  currentPage: number = 0;
  pageSize: number = 5;
  loading: boolean = false;
  Math = Math;


  constructor(private marvelService: MarvelService, private activatedRoute: ActivatedRoute){}

  ngOnInit(): void{
    this.loading = true;
    const nameStartsWith = this.activatedRoute.snapshot.queryParams['nameStartsWith'];
    this.marvelService.getCharacters(nameStartsWith).subscribe({
      next: (response: MarvelCharacter[]) => {
        console.log('data received');
        response.forEach(marvelCharacter => console.log(marvelCharacter));
        this.characters = response;
      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  get paginatedCharacters(): MarvelCharacter[]{
    const startIndex = this.currentPage * this.pageSize;
    const endIndex =  startIndex + this.pageSize;
    return this.characters.slice(startIndex, endIndex);
  }

// how come the UI refreshes with new characters when we click the next button, even though we never call paginatedCharacters?
//  Initially, currentPage is 0. The paginatedCharacters getter returns this.characters.slice(0, 5) (first 5 characters).
// You click "Next", which increments currentPage to 1.
// Angular's change detection notices that currentPage changed and re-evaluates the paginatedCharacters getter, now returning this.characters.slice(5, 10) (next 5 characters).
// The UI refreshes to display the next 5 characters, as *ngFor is bound to paginatedCharacters.
  nextPage() {
    if (this.currentPage < Math.ceil(this.characters.length / this.pageSize) - 1) {
      this.currentPage++;
    }
  }

  // Handle 'Previous' button click
  previousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
    }
  }
}
