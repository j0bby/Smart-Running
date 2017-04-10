import djchoices
from django.contrib.auth import get_user_model
from django.contrib.auth.models import AbstractUser
from django.core.validators import MaxValueValidator, MinValueValidator
from django.db import models


class ModeType(djchoices.DjangoChoices):
    TOURISTIC = djchoices.ChoiceItem()
    SPORTY = djchoices.ChoiceItem()


class ProfileType(djchoices.DjangoChoices):
    TOURISTIC = djchoices.ChoiceItem()
    SPORTY = djchoices.ChoiceItem()
    BOTH = djchoices.ChoiceItem()


class CustomUser(AbstractUser):
    profile = models.CharField(max_length=16, choices=ProfileType.choices)
    birth_date = models.DateField()
    email_verified = models.BooleanField(default=False)


class Route(models.Model):
    publisher = models.ForeignKey(get_user_model())

    date_published = models.DateTimeField(auto_now_add=True)
    last_updated = models.DateTimeField(auto_now=True)

    title = models.CharField(max_length=64)
    description = models.CharField(max_length=200)

    mode = models.CharField(max_length=16, choices=ModeType.choices)

    difficulty = models.IntegerField(validators=[
        MinValueValidator(1), MaxValueValidator(5)
    ])

    # markers list found in Marker model


class Marker(models.Model):
    title = models.CharField(max_length=64)
    description = models.CharField(max_length=200)
    full_description = models.TextField()

    latitude = models.DecimalField(max_digits=9, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)

    zone_radius = models.FloatField()

    routes = models.ManyToManyField(Route)
