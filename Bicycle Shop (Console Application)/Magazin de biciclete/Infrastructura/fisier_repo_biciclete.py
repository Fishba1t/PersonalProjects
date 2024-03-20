from copy import deepcopy

from Domain.Bicicleta import Bicicleta


class Fisier_Repo_Bicicleta:

    def __init__(self,lista_biciclete):
        self.lista_biciclete = lista_biciclete
        self.citire_fisier()

    def get_all_bicilete(self):
        return self.lista_biciclete

    def adauga_bicicleta(self,bicicleta):
        self.lista_undo = deepcopy(self.lista_biciclete)
        self.lista_biciclete.append(bicicleta)
        self.save_file()

    def sterge_tip(self,tip):
        self.lista_undo = deepcopy(self.lista_biciclete)
        i=0
        ok=0
        while i<len(self.lista_biciclete):
            if self.lista_biciclete[i].get_tip()==tip:
                self.lista_biciclete.remove(self.lista_biciclete[i])
                self.save_file()
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
                self.save_file()
                i=i-1
            i=i+1

    def undo_function(self):
        self.lista_biciclete=deepcopy(self.lista_undo)
        self.save_file()

    def citire_fisier(self):
        """
        functie care citeste din fisier
        :return:
        """
        try:
            with open("Infrastructura/fis_repo.txt", "r") as f:
                linie = f.readline().strip()
                while linie != "":
                    lista_elemente = linie.split(',')
                    id = int(lista_elemente[0])
                    tip = lista_elemente[1]
                    pret = float(lista_elemente[2])
                    bicicleta =Bicicleta(id,tip,pret)
                    self.lista_biciclete.append(bicicleta)
                    linie = f.readline().strip()
        except (ValueError, IndexError) as ve:
            print(str(ve))
        except FileNotFoundError:
            with open("Infrastructura/fis_repo.txt", 'x'):
                self.citire_fisier()

    def save_file(self):
        """
        functie care scrie in fisier
        :return:
        """
        try:
            with open("Infrastructura/fis_repo.txt", 'w') as f_bicicleta:
                for bicicleta in self.lista_biciclete:
                    f_bicicleta.write(f'{bicicleta.get_id()},{bicicleta.get_tip()},{bicicleta.get_pret()}\n')
        except ValueError:
            print('Eroare Valoare invalida')
        except IndexError:
            print('Eroare Tip de data invalid')
        except FileNotFoundError:
            print('Eroare Fisierul nu a fost gasit')
