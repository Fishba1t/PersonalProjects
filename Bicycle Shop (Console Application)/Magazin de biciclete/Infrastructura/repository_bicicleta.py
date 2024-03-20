from copy import deepcopy


class Repo_Bicicleta:

    def __init__(self,lista_biciclete):
        self.lista_biciclete = lista_biciclete
        self.lista_undo =[]

    def get_all_bicilete(self):
        return self.lista_biciclete

    def adauga_bicicleta(self,bicicleta):
        self.lista_undo.append(deepcopy(self.lista_biciclete))
        self.lista_biciclete.append(bicicleta)

    def sterge_tip(self,tip):
        self.lista_undo = deepcopy(self.lista_biciclete)
        i=0
        ok=0
        while i<len(self.lista_biciclete):
            if self.lista_biciclete[i].get_tip()==tip:
                self.lista_biciclete.remove(self.lista_biciclete[i])
                ok=1
                i=i-1
            i=i+1
        if ok==0:
            print("Nu exista biciclete de acest tip!")

    def sterge_max(self):
        self.lista_undo = deepcopy(self.lista_biciclete)
        maxim =0
        for bicicleta in self.lista_biciclete:
            if bicicleta.get_pret()>maxim:
                maxim=bicicleta.get_pret()
        i=0
        while i<len(self.lista_biciclete):
            if self.lista_biciclete[i].get_pret() == maxim:
                self.lista_biciclete.remove(self.lista_biciclete[i])
                i=i-1
            i=i+1

    def undo_function(self):
        self.lista_biciclete = deepcopy(self.lista_undo)