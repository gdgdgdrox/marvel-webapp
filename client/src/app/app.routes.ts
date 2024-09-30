import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { CharacterComponent } from './component/character/character.component';
import { CharacterDetailComponent } from './component/character-detail/character-detail.component';
import { NotFoundComponent } from './component/not-found/not-found.component';

export const routes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'characters', component: CharacterComponent},
    {path: 'character/:characterID', component: CharacterDetailComponent},
    {path: '**', component: NotFoundComponent}
];
