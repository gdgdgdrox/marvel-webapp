import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { MarvelCharacter } from '../interface/marvelcharacter';
import { MarvelCharacterDetail } from '../interface/marvelcharacterdetail';

@Injectable({
  providedIn: 'root'
})
export class MarvelService {

  constructor(private http: HttpClient) { }

  getCharacters(nameStartsWith: string): Observable<MarvelCharacter[]>{
    const params = new HttpParams().set("nameStartsWith",nameStartsWith)
    return this.http.get<MarvelCharacter[]>('http://localhost:8080/api/characters', {params});
  }

  getCharacterDetail(id: string): Observable<MarvelCharacterDetail>{
    return this.http.get<any>(`http://localhost:8080/api/character/${id}`).pipe(
      map(data => {
        console.log('data is ', data);
        return {
          name: data['name'],
          id: data['id'],
          description: data['description'],
          picture: data['imageURL']
        }
      })
    )
  }
}
