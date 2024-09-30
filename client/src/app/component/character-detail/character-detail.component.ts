import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { MarvelService } from '../../service/marvel.service';
import { MarvelCharacterDetail } from '../../interface/marvelcharacterdetail';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-character-detail',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './character-detail.component.html',
  styleUrl: './character-detail.component.css'
})
export class CharacterDetailComponent implements OnInit{

  marvelCharacterDetail?: MarvelCharacterDetail;

  constructor(private route: ActivatedRoute, private marvelService: MarvelService){}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('characterID') ?? '';
    console.log(`id is ${id}`);
    this.marvelService.getCharacterDetail(id)
      .subscribe({
        next: (data: MarvelCharacterDetail) =>{
          console.log('data received');
          console.log(data);
          this.marvelCharacterDetail = data;
        },
        error: (error: HttpErrorResponse) => {
          console.error(error);
        }
      })

  }


}
