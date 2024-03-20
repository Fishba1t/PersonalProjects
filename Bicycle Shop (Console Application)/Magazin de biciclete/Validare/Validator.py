
class Validator:

    def validare_bicicleta(self,id, tip, pret, lista_biciclete):
        eroare = []
        lista_tip = ["Mountain","BMX","Road"]
        for bicileta in lista_biciclete:
            if bicileta.get_id()==id:
                eroare.append("ID indisponibil!Introduceti un ID valid!")
        if tip[0].islower():
            eroare.append("Initiala tipului trebuie sa fie o litera mare!")
        if tip not in lista_tip:
            eroare.append("Tip indisponibil!")
        if float(pret)==False:
            eroare.append("Pretul trebuie sa fie float!")
        if int(pret)>2000:
            eroare.append("Nu exista bicilete cu acest pret!")
        if int(pret)<0:
            eroare.append("Pretul nu poate fi un numar negativ!")
        if len(eroare)>0:
            eroare_string = '\n'.join(eroare)
            raise ValueError(eroare_string)

    def validare_tip(self,tip):
        eroare=[]
        lista_tip = ["Mountain", "BMX", "Road"]
        if tip[0].islower():
            eroare.append("Initiala tipului trebuie sa fie o litera mare!")
        if tip not in lista_tip:
            eroare.append("Tip indisponibil!")
        if len(eroare)>0:
            eroare_string = '\n'.join(eroare)
            raise ValueError(eroare_string)