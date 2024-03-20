from Domain.Bicicleta import Bicicleta
from Infrastructura.repository_bicicleta import Repo_Bicicleta
from Validare.Validator import Validator


class Service_Bicicleta:

    def __init__(self,repo:Repo_Bicicleta, validare=Validator):
        self.repo = repo
        self.validare = validare

    def get_all_biciclete(self):
        return self.repo.lista_biciclete

    def adauga_bicileta(self,id,tip,pret):
        self.validare.validare_bicicleta(id, tip, pret, self.repo.lista_biciclete)
        bicicleta =Bicicleta(id,tip,pret)
        self.repo.adauga_bicicleta(bicicleta)

    def sterge_tip(self,tip):
        self.validare.validare_tip(tip)
        self.repo.sterge_tip(tip)

    def sterge_max(self):
        self.repo.sterge_max()

    def undo_function(self):
        self.repo.undo_function()
