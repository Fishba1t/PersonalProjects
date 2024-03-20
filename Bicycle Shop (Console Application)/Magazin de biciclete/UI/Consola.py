from Buissiness.service_biciclete import Service_Bicicleta


class Consola:

    def __init__(self,serv:Service_Bicicleta):
        self.serv= serv

    def comenzi_valide(self):
        print("Comenzile sunt: 1=Adauga Bicicleta,"
              "2=Afiseaza Toate Bicicletele,"
              "3=Sterge Tip,"
              "4=Sterge bicicleta cu pretul maxim,"
              "5=Undo(one time for each functionality),"
              "6=exit")

    def executa_comenzi(self):
        self.comenzi_valide()
        nume_comanda = input("Introduceti numarul comenzii:")
        if nume_comanda =="1":
            try:
                id = int(input("Introduceti ID ul bicicletei:"))
                tip = input("Introduceti tipul bicicletei:")
                pret = float(input("Introduceti pretul biciletei:"))
                self.serv.adauga_bicileta(id, tip, pret)
            except ValueError as ve:
                print(str(ve))
        elif nume_comanda == "2":
            try:
                for bicicleta in self.serv.get_all_biciclete():
                    print(bicicleta)
            except ValueError as ve:
                print(str(ve))
        elif nume_comanda=="3":
            try:
                tip=input("Introdceti tipul bicicletelor pe care doriti sa le stergeti:")
                self.serv.sterge_tip(tip)
            except ValueError as ve:
                 print(str(ve))
        elif nume_comanda=="4":
            try:
                self.serv.sterge_max()
            except ValueError as ve:
                 print(str(ve))
        elif nume_comanda=="5":
            try:
                self.serv.undo_function()
            except ValueError as ve:
                 print(str(ve))
        elif nume_comanda=="6":
            try:
                return
            except ValueError as ve:
                 print(str(ve))
        self.executa_comenzi()